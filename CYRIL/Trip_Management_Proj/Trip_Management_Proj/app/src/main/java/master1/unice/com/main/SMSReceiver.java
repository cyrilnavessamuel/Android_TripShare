package master1.unice.com.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

    private  static SMSListener smsListener;


    @Override
    public void onReceive(Context context, Intent intent) {
        String messageBody ="";
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    messageBody += smsMessage.getMessageBody();

                }
            } else {
                Bundle bundle = intent.getExtras();
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) (pdus[i]), format);
                    messageBody = smsMessage.getMessageBody();
                }
            }
                smsListener.messageReceived(messageBody);
        }
    }

    public  static void setSmsListener(SMSListener sMSListener){
        smsListener = sMSListener;
    }
}
