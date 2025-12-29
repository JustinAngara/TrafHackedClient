package com.traf;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinUser.INPUT;

public final class HandleMouseOutput {

    private static final User32 USER32 = User32.INSTANCE;

    public static final int MOUSEEVENTF_LEFTDOWN = 2;
    public static final int MOUSEEVENTF_LEFTUP   = 4;

    private static final int VK_SPACE = 0x20;


    // pre-init everything
    private static final INPUT[] LEFT_CLICK_INPUTS;
    static{
        LEFT_CLICK_INPUTS = (INPUT[]) new INPUT().toArray(2);

        // LEFT DOWN
        LEFT_CLICK_INPUTS[0].type = new DWORD(INPUT.INPUT_MOUSE);
        LEFT_CLICK_INPUTS[0].input.setType("mi");
        LEFT_CLICK_INPUTS[0].input.mi.dwFlags = new DWORD(MOUSEEVENTF_LEFTDOWN);
        LEFT_CLICK_INPUTS[0].input.mi.time = new DWORD(0);
        LEFT_CLICK_INPUTS[0].input.mi.dwExtraInfo = new BaseTSD.ULONG_PTR(0);

        // LEFT UP
        LEFT_CLICK_INPUTS[1].type = new DWORD(INPUT.INPUT_MOUSE);
        LEFT_CLICK_INPUTS[1].input.setType("mi");
        LEFT_CLICK_INPUTS[1].input.mi.dwFlags = new DWORD(MOUSEEVENTF_LEFTUP);
        LEFT_CLICK_INPUTS[1].input.mi.time = new DWORD(0);
        LEFT_CLICK_INPUTS[1].input.mi.dwExtraInfo = new BaseTSD.ULONG_PTR(0);
    }

    public static void leftClick() {
        USER32.SendInput(
                new DWORD(LEFT_CLICK_INPUTS.length),
                LEFT_CLICK_INPUTS,
                LEFT_CLICK_INPUTS[0].size()
        );
    }

    public static boolean isSpaceHeld() {
        return (USER32.GetAsyncKeyState(VK_SPACE) & 0x8000) != 0;
    }
}
