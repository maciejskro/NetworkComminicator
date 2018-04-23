package com.sda.server;

import com.sda.client.ContactList;

import java.util.Stack;

public class ProxyHelper  {

    private Stack<ContactList> stack;

    public ProxyHelper() {
        this.stack = new Stack<>();

    }
    public ContactList pop() {
        return stack.pop();
    }

    public void push(ContactList cl) {
        this.push(cl);
    }
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
