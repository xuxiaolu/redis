package com.xuxl.redis.common.dto;

import java.io.Serializable;
import java.util.Objects;

public class Cluster implements Serializable {

    private static final long serialVersionUID = 6180070372251087998L;

    private String hostAndPorts;

    private String password;

    public String getHostAndPorts() {
        return hostAndPorts;
    }

    public void setHostAndPorts(String hostAndPorts) {
        this.hostAndPorts = hostAndPorts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster that = (Cluster) o;
        return Objects.equals(hostAndPorts, that.hostAndPorts) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostAndPorts, password);
    }

    @Override
    public String toString() {
        return "Cluster{" +
                "hostAndPorts='" + hostAndPorts + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
