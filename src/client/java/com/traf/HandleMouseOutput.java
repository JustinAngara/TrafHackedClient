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

    private static final int VK_W = 0x57;
    private static final int VK_A = 0x41;
    private static final int VK_S = 0x53;
    private static final int VK_D = 0x44;

    private static final int W = 0, A = 1, S = 2, D = 3;
    private static final int[] vkIterate = { VK_W, VK_A, VK_S, VK_D };


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

    public static int[] getMovementHeld(){
        // wasd (+, -, -, +)
        int[] temp = { 0, 0, 0, 0 };

        for(int i = 0; i < temp.length; i++){
            temp[i] = isKeyHeld(vkIterate[i]) ? 1 : 0;
        }

        // canceling out logic, WS and AD should cancel out
        if(temp[W] == temp[S]){
            temp[W] = 0;
            temp[S] = 0;
        }
        if(temp[A] == temp[D]){
            temp[A] = 0;
            temp[D] = 0;
        }

        return temp;
    }

    /**
     * pair array 2 values max
     *
     * [1,1] meaning 1 in the positive x and positive in the z
     * [-1,-1] meaning 1 in the negative x and negative in the z
     * */
    public static int[] getDirection(){
        int[] movement = getMovementHeld();
        int[] pair = new int[2]; // {x, z}
        int v1=0, v2=0;


        // x will be located in 0, 2
        if( movement[S]!=movement[W] && movement[W] == 1 ){
            v1 = 1;
        } else if( movement[S]!=movement[W] && movement[S] == 1 ) {
            v1 = -1;
        }

        // z will be located in 1, 3
        if( movement[A]!=movement[D] && movement[D] == 1 ){
            v2 = 1;
        } else if (movement[A]!=movement[D] && movement[A] == 1) {
            v2 = -1;
        }

        pair[0] = v1;
        pair[1] = v2;
        return pair;
    }

    public static boolean isKeyHeld(int vk){
        return (USER32.GetAsyncKeyState(vk) & 0x8000) != 0;
    }

    public static boolean isSpaceHeld() {
        return (USER32.GetAsyncKeyState(VK_SPACE) & 0x8000) != 0;
    }
}
