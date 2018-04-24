package com.sda.server;

import com.sda.client.ContactList;

import java.util.Stack;

public class ProxyHelper  {

    private Stack<ContactList> stack;

    ProxyHelper() {
        this.stack = new Stack<>();
    }

    ContactList pop() {
        return stack.pop();
    }
    void push(ContactList cl) {
        this.push(cl);
    }
    boolean isEmpty() {
        return stack.isEmpty();
    }



}
