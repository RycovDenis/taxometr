package com.example.taxometr.Controller;

import com.example.taxometr.R;
import com.example.taxometr.R.array;
import com.example.taxometr.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class messageController extends DialogFragment implements OnClickListener {
	AlertDialog.Builder adb;//�������� ���������, ������������ ������ ������������
	int type, msg;//type - ��� ������(� ����� ������� ��� �����), msg - ��������� ��� ������������, �������� � String.xml
	private int res = -1;//��������� ��� ������ ����������
	String[] message;//������ ����� ���������
	String tag;//������������ ��� ���������� ����������
	private OnDismissListener mCallback;//����� �� ����� ������, ��� ������ ������������
	private soundController sound;//���������� ������
	private Context context;//����������� �������� ��� ������ 
	
	public messageController(Context cnt){
		sound = new soundController(cnt);
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		tag = getResources().getString(R.string.tag);
		message = getResources().getStringArray(R.array.msg);
		
		//get args
		super.onCreate(savedInstanceState);
		type = getArguments().getInt("type");//������� ��� ������ (���� ������ ��� ���)
		msg = getArguments().getInt("msg");//�������� id ���������, ������� �������� � String.xml

		//set title and body for message
		adb = new AlertDialog.Builder(getActivity())
		.setTitle(R.string.warning)//������������� ��������� ���������
		.setMessage(message[msg]);//������������� ���� ���������
		
		switch(type){
		case 1: 	//��������� ��� ������ ��� ���������
		    adb.setPositiveButton(R.string.yes, this)
	        .setNegativeButton(R.string.no, this)
	        .setNeutralButton(R.string.cancel, this);
		break;
		case 2: 	//��������� ���� ������ ��� ���������
		    adb.setPositiveButton(R.string.ok, this);
		break;
			
		default:
			break;
				
		}
		
		sound.play(1);//����������� ����
	    return adb.create();//���������� ���� ���������
	}
	

	@Override
	public void onClick(DialogInterface Dialog, int i) {//������ �� ����� ������ ����� ������������
		switch(i){
			case -1:
				res = 1;
				break;
				
			case -2:
				res = 2;
				break;
				
			case -3:
				res = 0;
				break;
				
			default:
				break;
		}
		

	    mCallback.OnDismissListener(res);
	    res = -1;
	}
	
	public interface OnDismissListener {//���������� ��� ������
	    public void OnDismissListener(int res);
	}

	@Override
	public void onAttach(Activity activity) {//���������� ��� ������
	    super.onAttach(activity);
	    mCallback = (OnDismissListener) activity;
	}

	
}
