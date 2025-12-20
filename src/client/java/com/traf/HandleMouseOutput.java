package com.traf;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.INPUT;

public final class HandleMouseOutput {

    private static final User32 USER32 = User32.INSTANCE;
    public static final int MOUSEEVENTF_LEFTDOWN = 2;
    public static final int MOUSEEVENTF_LEFTUP   = 4;

    private HandleMouseOutput() {}

    public static void leftClick() {
        INPUT[] inputs = (INPUT[]) new INPUT().toArray(2);

        // LEFT DOWN
        inputs[0].type = new DWORD(INPUT.INPUT_MOUSE);
        inputs[0].input.setType("mi");
        inputs[0].input.mi.dwFlags = new DWORD(MOUSEEVENTF_LEFTDOWN);
        inputs[0].input.mi.time = new DWORD(0);
        inputs[0].input.mi.dwExtraInfo = new BaseTSD.ULONG_PTR(0);

        // LEFT UP
        inputs[1].type = new DWORD(INPUT.INPUT_MOUSE);
        inputs[1].input.setType("mi");
        inputs[1].input.mi.dwFlags = new DWORD(MOUSEEVENTF_LEFTUP);
        inputs[1].input.mi.time = new DWORD(0);
        inputs[1].input.mi.dwExtraInfo = new BaseTSD.ULONG_PTR(0);

        USER32.SendInput(
                new DWORD(inputs.length),
                inputs,
                inputs[0].size()
        );
    }


}
