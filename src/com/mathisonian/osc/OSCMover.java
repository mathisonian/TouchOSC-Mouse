package com.mathisonian.osc;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Date;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class OSCMover {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final Robot robot = new Robot();
			OSCPortIn receiver = new OSCPortIn(44100);
			OSCListener accelListener = new OSCListener() {
				
				@Override
				public void acceptMessage(Date time, OSCMessage message) {
					Object[] arguments = message.getArguments();
					Float x, y; //, z;
					try {
						x = (Float) arguments[0];
						y = (Float) arguments[1];
//						z = (Float) arguments[2];
					} catch(Exception e) {
						return;
					}
					
					// ROBOTS!!
					PointerInfo a = MouseInfo.getPointerInfo();
					Point b;
					try {
						b = a.getLocation();
					} catch (Exception e) {
						return;
					}
					int mouseX = (int) b.getX();
					int mouseY = (int) b.getY();
					
					robot.mouseMove((int) (mouseX + 20*x), (int) (mouseY + 20*y));
				}
			};
			
			OSCListener leftButtonListener = new OSCListener() {
				@Override
				public void acceptMessage(Date time, OSCMessage message) {
					Object[] arguments = message.getArguments();
					Float isOn;
					try {
						isOn = (Float) arguments[0];
					} catch(Exception e) {
						e.printStackTrace();
						return;
					}

					if(isOn > 0.5) {
						robot.mousePress(InputEvent.BUTTON1_MASK);						
					} else {
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
					}
				}
			};
			
			OSCListener rightButtonListener = new OSCListener() {
				@Override
				public void acceptMessage(Date time, OSCMessage message) {
					Object[] arguments = message.getArguments();
					Float isOn;
					try {
						isOn = (Float) arguments[0];
					} catch(Exception e) {
						e.printStackTrace();
						return;
					}
					
					if(isOn > 0.5) {
						robot.mousePress(InputEvent.BUTTON3_MASK);						
					} else {
						robot.mouseRelease(InputEvent.BUTTON3_MASK);
					}
				}
			};
			receiver.addListener("/accxyz", accelListener);
			receiver.addListener("/1/push6", leftButtonListener);
			receiver.addListener("/1/push9", rightButtonListener);
			receiver.startListening();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
