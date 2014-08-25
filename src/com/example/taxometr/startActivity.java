package com.example.taxometr;

import com.example.taxometr.Controller.formController;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

public class startActivity extends Activity {
	Bundle args;
	SharedPreferences sPref;
	CheckBox local;
	formController form;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                        
        form = new formController(this);
        
        sPref = getSharedPreferences("settings", MODE_PRIVATE);// ��������� ��������� (����������)
        
        if(form.db.countDrivers() < 2 && sPref.getBoolean("local", false)) 
        	//���� ���� ������������ � ����� "����������", �� ��������� �� ���� ������
        	form.showForm(2, this, "");
        else // ����� ��������� �� ���� �����������
        	form.showForm(1, this, "");
    }

}