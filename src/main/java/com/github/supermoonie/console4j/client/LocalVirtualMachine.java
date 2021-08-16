package com.github.supermoonie.console4j.client;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import lombok.extern.slf4j.Slf4j;
import sun.jvmstat.monitor.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author supermoonie
 * @since 2021/8/16
 */
@Slf4j
public class LocalVirtualMachine {

    private String address;
    private String commandLine;
    private String displayName;
    private int vmId;
    private boolean isAttachSupported;

    public LocalVirtualMachine(int vmId, String commandLine, boolean canAttach, String connectorAddress) {
        this.vmId = vmId;
        this.commandLine = commandLine;
        this.address = connectorAddress;
        this.isAttachSupported = canAttach;
        this.displayName = getDisplayName(commandLine);
    }

    private static String getDisplayName(String commandLine) {
        // trim the pathname of jar file if it's a jar
        String[] res = commandLine.split(" ", 2);
        if (res[0].endsWith(".jar")) {
            File jarfile = new File(res[0]);
            String displayName = jarfile.getName();
            if (res.length == 2) {
                displayName += " " + res[1];
            }
            return displayName;
        }
        return commandLine;
    }

    public int vmId() {
        return vmId;
    }

    public boolean isManageable() {
        return (address != null);
    }

    public boolean isAttachable() {
        return isAttachSupported;
    }

    public void startManagementAgent() throws IOException {
        if (address != null) {
            // already started
            return;
        }

        if (!isAttachable()) {
            throw new IOException("This virtual machine \"" + vmId +
                    "\" does not support dynamic attach.");
        }

        loadManagementAgent();
        // fails to load or start the management agent
        if (address == null) {
            // should never reach here
            throw new IOException("Fails to find connector address");
        }
    }

    public String connectorAddress() {
        // return null if not available or no JMX agent
        return address;
    }

    public String displayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return commandLine;
    }

    // This method returns the list of all virtual machines currently
    // running on the machine
    public static Map<Integer, LocalVirtualMachine> getAllVirtualMachines() {
        Map<Integer, LocalVirtualMachine> map =
                new HashMap<Integer, LocalVirtualMachine>();
        getMonitoredVMs(map);
        getAttachableVMs(map);
        return map;
    }

    private static void getMonitoredVMs(Map<Integer, LocalVirtualMachine> map) {
        MonitoredHost host;
        Set<Integer> vms;
        try {
            host = MonitoredHost.getMonitoredHost(new HostIdentifier((String)null));
            vms = host.activeVms();
        } catch (java.net.URISyntaxException | MonitorException x) {
            throw new InternalError(x.getMessage(), x);
        }
        for (Integer vmId: vms) {
            if (vmId != null) {
                int pid = vmId;
                String name = vmId.toString(); // default to pid if name not available
                boolean attachable = false;
                String address = null;
                try {
                    MonitoredVm mvm = host.getMonitoredVm(new VmIdentifier(name));
                    // use the command line as the display name
                    name =  MonitoredVmUtil.commandLine(mvm);
                    attachable = MonitoredVmUtil.isAttachable(mvm);

                    VirtualMachine virtualMachine = VirtualMachine.attach(Integer.toString(pid));
                    Properties properties = virtualMachine.getAgentProperties();
                    address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
                    mvm.detach();
                } catch (Exception x) {
                    // ignore
                }
                map.put(vmId,
                        new LocalVirtualMachine(pid, name, attachable, address));
            }
        }
    }

    private static final String LOCAL_CONNECTOR_ADDRESS_PROP =
            "com.sun.management.jmxremote.localConnectorAddress";

    private static void getAttachableVMs(Map<Integer, LocalVirtualMachine> map) {
        List<VirtualMachineDescriptor> vms = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : vms) {
            try {
                Integer vmid = Integer.valueOf(vmd.id());
                if (!map.containsKey(vmid)) {
                    boolean attachable = false;
                    String address = null;
                    try {
                        VirtualMachine vm = VirtualMachine.attach(vmd);
                        attachable = true;
                        Properties agentProps = vm.getAgentProperties();
                        address = (String) agentProps.get(LOCAL_CONNECTOR_ADDRESS_PROP);
                        vm.detach();
                    } catch (AttachNotSupportedException x) {
                        // not attachable
                    } catch (IOException x) {
                        // ignore
                    }
                    map.put(vmid, new LocalVirtualMachine(vmid,
                            vmd.displayName(),
                            attachable,
                            address));
                }
            } catch (NumberFormatException e) {
                // do not support vmid different than pid
            }
        }
    }

    public static LocalVirtualMachine getLocalVirtualMachine(int vmid) {
        Map<Integer, LocalVirtualMachine> map = getAllVirtualMachines();
        LocalVirtualMachine lvm = map.get(vmid);
        if (lvm == null) {
            // Check if the VM is attachable but not included in the list
            // if it's running with a different security context.
            // For example, Windows services running
            // local SYSTEM account are attachable if you have Adminstrator
            // privileges.
            boolean attachable = false;
            String address = null;
            String name = String.valueOf(vmid); // default display name to pid
            try {
                VirtualMachine vm = VirtualMachine.attach(name);
                attachable = true;
                Properties agentProps = vm.getAgentProperties();
                address = (String) agentProps.get(LOCAL_CONNECTOR_ADDRESS_PROP);
                vm.detach();
                lvm = new LocalVirtualMachine(vmid, name, attachable, address);
            } catch (AttachNotSupportedException | IOException x) {
                log.error(x.getMessage(), x);
            }
        }
        return lvm;
    }

    // load the management agent into the target VM
    private void loadManagementAgent() throws IOException {
        VirtualMachine vm = null;
        String name = String.valueOf(vmId);
        try {
            vm = VirtualMachine.attach(name);
        } catch (AttachNotSupportedException x) {
            throw new IOException(x.getMessage(), x);
        }

        vm.startLocalManagementAgent();

        // get the connector address
        Properties agentProps = vm.getAgentProperties();
        address = (String) agentProps.get(LOCAL_CONNECTOR_ADDRESS_PROP);

        vm.detach();
    }
}
