package com.tuti.test;

import java.io.IOException;

import android.os.RemoteException;
import android.util.Log;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class WchatTest extends UiAutomatorTestCase {

	private static final int STEPS = 36;
	private static final String WX_PREFIX = "微信号: ";

	public void jumpContact() throws UiObjectNotFoundException, RemoteException {
		UiDevice device = getUiDevice();
		device.wakeUp();
		assertTrue("screenOn: can't wakeup", device.isScreenOn());
		device.pressHome();
		sleep(1000);

		try {
			Runtime.getRuntime().exec(
					"am start -n com.tencent.mm/.ui.LauncherUI");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sleep(1000);

		// WxViewPager
		UiObject wxViewPager = new UiObject(
				new UiSelector()
						.className("com.tencent.mm.ui.mogic.WxViewPager"));
		Log.d("mp", "WxViewPager :" + wxViewPager);

		UiObject bottomBar = wxViewPager.getFromParent(new UiSelector()
				.className("android.widget.RelativeLayout"));
		Log.d("mp", "bottomBar :" + bottomBar);

		UiObject bottomBarLayout = bottomBar.getChild(new UiSelector()
				.className("android.widget.LinearLayout"));
		Log.d("mp", "bottomBarLayout :" + bottomBarLayout);

		UiObject contactLayout = bottomBarLayout.getChild(new UiSelector()
				.className("android.widget.RelativeLayout").index(1));
		Log.d("mp", "contactLayout :" + contactLayout.getBounds());

		sleep(100);
		contactLayout.click();
	}

	private void getGroupInof() throws UiObjectNotFoundException {
		UiDevice device = getUiDevice();
		UiScrollable contactList = new UiScrollable(
				new UiSelector().resourceId("android:id/list"));

		int childCount = contactList.getChildCount(new UiSelector()
				.resourceId("com.tencent.mm:id/cz1"));
		Log.d("mp", "--parent-childCount:" + childCount);
		for (int i = 0; i < childCount; i++) {
			UiObject child = contactList.getChild(new UiSelector().className(
					"android.widget.LinearLayout").index(i));

			child.click();
			sleep(100);

			UiObject wxText = new UiObject(
					new UiSelector().textContains(WX_PREFIX));
			String wx_account = wxText.getText().replace(WX_PREFIX, "");
			Log.d("mp2", "----wx_account:" + wx_account);

			device.pressBack();
		}

		// 继续滑动,判断是否显示了XX位联系人
		UiObject endObject = new UiObject(
				new UiSelector().resourceId("com.tencent.mm:id/amy"));
		if (!endObject.exists()) {
			contactList.scrollForward(STEPS);

			getContactInfo();
		}
	}

	private void getContactInfo() throws UiObjectNotFoundException {
		UiDevice device = getUiDevice();
		UiScrollable contactList = new UiScrollable(
				new UiSelector().resourceId("com.tencent.mm:id/j8"));

		int childCount = contactList.getChildCount();
		Log.d("mp", "--parent-childCount:" + childCount);
		for (int i = 0; i < childCount; i++) {
			UiObject child = contactList.getChild(new UiSelector().className(
					"android.widget.LinearLayout").index(i));

			if (child.getChildCount() <= 2) {

				// 只有2个以下child的才是用户
				UiObject contact = child.getChild(new UiSelector()
						.resourceId("com.tencent.mm:id/jq"));
				Log.d("mp", i + "---real-child:" + child.getChildCount()
						+ "   " + contact.getText());

				contact.click();
				sleep(100);

				UiObject wxText = new UiObject(
						new UiSelector().textContains(WX_PREFIX));
				String wx_account = wxText.getText().replace(WX_PREFIX, "");
				Log.d("mp2", "----wx_account:" + wx_account);

				device.pressBack();
			}
		}

		// 继续滑动,判断是否显示了XX位联系人
		UiObject endObject = new UiObject(
				new UiSelector().resourceId("com.tencent.mm:id/amy"));
		if (!endObject.exists()) {
			contactList.scrollForward(STEPS);

			getContactInfo();
		}

	}

	public void testDemo() throws UiObjectNotFoundException, RemoteException {

		// 跳转微信通讯录
		// this.jumpContact();

		// getContactInfo();

		getGroupInof();
	}
}
