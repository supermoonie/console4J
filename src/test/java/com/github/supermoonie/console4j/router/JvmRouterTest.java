package com.github.supermoonie.console4j.router;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.junit.Test;

import java.util.List;

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

}