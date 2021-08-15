package com.github.supermoonie.console4j.router;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.junit.Test;
import sun.management.ConnectorAddressLink;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author super_w
 * @since 2021/8/13
 */
public class JvmRouterTest {

    @Test
    public void localJvm() {
        List<VirtualMachineDescriptor> vms = VirtualMachine.list();
        for (VirtualMachineDescriptor vm : vms) {
            String id = vm.id();
            String name = vm.displayName();
            System.out.println("id: " + id + ", name: " + name);
        }
    }

    @Test
    public void attachLocalJvm() throws Exception {
        int pid = 15601;
        String address = ConnectorAddressLink.importFrom(pid);
        if (null == address) {
            VirtualMachine virtualMachine = VirtualMachine.attach(Integer.toString(pid));
            String url = virtualMachine.startLocalManagementAgent();
            Properties properties = virtualMachine.getAgentProperties();
            address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
        }
        JMXServiceURL jmxServiceURL = new JMXServiceURL(address);
        JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL, null);

    }

}