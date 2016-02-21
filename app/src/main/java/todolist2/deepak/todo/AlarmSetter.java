package todolist2.deepak.todo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Created by Deepak on 20-Feb-16.
 */

public class AlarmSetter extends BroadcastReceiver {
	
//	private static final String TAG = "AlarmSetter";

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, AlarmService.class);
		service.setAction(AlarmService.CREATE);
		context.startService(service);
	}

}
