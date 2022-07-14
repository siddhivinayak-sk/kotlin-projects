package com.sk.ktl.basic;

public class Organizer {

    boolean closeMeeting(XMeeting meeting) {
        if(meeting.canClose) {
            return meeting.close();
        }
        return false;
    }

    public static void main(String...args) {
        Organizer organizer = new Organizer();
        organizer.closeMeeting(null);  //Throws NullPointerException
    }
}

class XMeeting {
    public boolean canClose;

    public boolean close() {
        return false;
    }
}
