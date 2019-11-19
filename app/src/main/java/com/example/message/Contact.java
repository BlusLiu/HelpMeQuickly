package com.example.message;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 15:57 2019/11/19
 */
public class Contact {
    String portrait;
    String name;

    String desc;

    public Contact(String portrait, String name, String desc) {
        this.portrait = portrait;
        this.name = name;
        this.desc = desc;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
