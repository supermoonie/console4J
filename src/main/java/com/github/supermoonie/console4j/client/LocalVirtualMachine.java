package com.github.supermoonie.console4j.client;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import lombok.extern.slf4j.Slf4j;
import sun.jvmstat.monitor.*;
import sun.management.ConnectorAddressLink;

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
    public static Map<Integer, sun.tools.jconsole.LocalVirtualMachine> getAllVirtualMachines() {
        Map<Integer, sun.tools.jconsole.LocalVirtualMachine> map =
                new HashMap<Integer, sun.tools.jconsole.LocalVirtualMachine>();
        getMonitoredVMs(map);
        getAttachableVMs(map);
        return map;
    }

    private static void getMonitoredVMs(Map<Integer, sun.tools.jconsole.LocalVirtualMachine> map) {
        MonitoredHost host;
        Set<Integer> vms;
        try {
            host = MonitoredHost.getMonitoredHost(new HostIdentifier((String)null));
            vms = host.activeVms();
        } catch (java.net.URISyntaxException | MonitorException x) {
            throw new InternalError(x.getMessage(), x);
        }
        for (Object vmid: vms) {
            if (vmid instanceof Integer) {
                int pid = ((Integer) vmid).intValue();
                String name = vmid.toString(); // default to pid if name not available
                boolean attachable = false;
                String address = null;
                try {
                    MonitoredVm mvm = host.getMonitoredVm(new VmIdentifier(name));
                    // use the command line as the display name
                    name =  MonitoredVmUtil.commandLine(mvm);
                    attachable = MonitoredVmUtil.isAttachable(mvm);
                    address = ConnectorAddressLink.importFrom(pid);
                    mvm.detach();
                } catch (Exception x) {
                    // ignore
                }
                map.put((Integer) vmid,
                        new sun.tools.jconsole.LocalVirtualMachine(pid, name, attachable, address));
            }
        }
    }

    private static final String LOCAL_CONNECTOR_ADDRESS_PROP =
            "com.sun.management.jmxremote.localConnectorAddress";

    private static void getAttachableVMs(Map<Integer, sun.tools.jconsole.LocalVirtualMachine> map) {
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
                    map.put(vmid, new sun.tools.jconsole.LocalVirtualMachine(vmid.intValue(),
                            vmd.displayName(),
                            attachable,
                            address));
                }
            } catch (NumberFormatException e) {
                // do not support vmid different than pid
            }
        }
    }

    public static sun.tools.jconsole.LocalVirtualMachine getLocalVirtualMachine(int vmid) {
        Map<Integer, sun.tools.jconsole.LocalVirtualMachine> map = getAllVirtualMachines();
        sun.tools.jconsole.LocalVirtualMachine lvm = map.get(vmid);
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
                lvm = new sun.tools.jconsole.LocalVirtualMachine(vmid, name, attachable, address);
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
            IOException ioe = new IOException(x.getMessage());
            ioe.initCause(x);
            throw ioe;
        }

        vm.startLocalManagementAgent();

        // get the connector address
        Properties agentProps = vm.getAgentProperties();
        address = (String) agentProps.get(LOCAL_CONNECTOR_ADDRESS_PROP);

        vm.detach();
    }
}
