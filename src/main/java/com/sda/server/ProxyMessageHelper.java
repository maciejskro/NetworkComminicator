package com.sda.server;

import com.sda.client.ContactList;

import java.util.Stack;

public class ProxyMessageHelper {

    private Stack<ContactList> stack;

    ProxyMessageHelper() {
        this.stack = new Stack<>();
    }

    ContactList pop() {
        return stack.pop();
    }
    void push(ContactList cl) {
        stack.push(cl);
    }
    boolean isEmpty() {
        return stack.isEmpty();
    }



}
