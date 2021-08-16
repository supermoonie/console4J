package com.github.supermoonie.console4j.router;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.junit.Test;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
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
        VirtualMachine virtualMachine = VirtualMachine.attach(Integer.toString(pid));
        String url = virtualMachine.startLocalManagementAgent();
        System.out.println(url);
        Properties properties = virtualMachine.getAgentProperties();
        String address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
        JMXServiceURL jmxServiceURL = new JMXServiceURL(address);
        JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL, null);
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

    }

}