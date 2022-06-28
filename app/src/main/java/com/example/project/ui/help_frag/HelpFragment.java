package com.example.project.ui.help_frag;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.R;
import com.example.project.databinding.FragmentHelpBinding;

public class HelpFragment extends Fragment {


    private HelpViewModel helpViewModel;
    private FragmentHelpBinding binding;
    private Button button;
    final Context context = getActivity();
    private TextView final_text;
    Dialog dialog;
    EditText etSubject,etMessage;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helpViewModel =
                new ViewModelProvider(this).get(HelpViewModel.class);

        binding = FragmentHelpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();






        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());



        mDialogBuilder
                        .setView(R.layout.prompt)
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        //Вводим текст и отображаем в строке ввода на основном экране:
                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = mDialogBuilder.create();



        etSubject=root.findViewById(R.id.et_subject);
        etMessage=root.findViewById(R.id.et_message);
        button = (Button) root.findViewById(R.id.button2);

        View.OnClickListener cl = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"help_analizator@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                emailIntent.setSelector( emailSelectorIntent );


                startActivity(emailIntent);
                etSubject.setText("");
                etMessage.setText("");


               //alertDialog.show();
//                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
//                LayoutInflater li = LayoutInflater.from(context);
//                View promptsView = li.inflate(R.layout.prompt, null);
//
//                //Создаем AlertDialog
//                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
//
//                //Настраиваем prompt.xml для нашего AlertDialog:
//                mDialogBuilder.setView(promptsView);
//
//                //Настраиваем отображение поля для ввода текста в открытом диалоге:
//                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
//
//                //Настраиваем сообщение в диалоговом окне:
//                mDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        //Вводим текст и отображаем в строке ввода на основном экране:
//                                        final_text.setText(userInput.getText());
//                                    }
//                                })
//                        .setNegativeButton("Отмена",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                AlertDialog alertDialog = mDialogBuilder.create();
//
//                //и отображаем его:
//                alertDialog.show();

            }
        };
        button.setOnClickListener(cl);



        helpViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}