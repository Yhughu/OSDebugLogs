
package com.outsystems.plugins.debug.console;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class OSConsoleFragment extends Fragment implements OSConsoleCommands {

    private OSUiConsoleInterface mListener;
    private TextView mTextView;

    public static OSConsoleFragment newInstance() {
        return new OSConsoleFragment();
    }

    public void clear() {
        if (this.mTextView != null) {
            this.mTextView.setText("");
        }
    }

    public void log(String output) {
        if (this.mTextView != null) {
            this.mTextView.append("\n" + output);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {

        int fragment_console = getActivity().getResources().getIdentifier("fragment_console", "layout", getActivity().getPackageName());

        View view = paramLayoutInflater.inflate(fragment_console, paramViewGroup, false);

        int txtConsoleId = view.getResources().getIdentifier("txtConsole","id",getActivity().getPackageName());
        int btnClearId = view.getResources().getIdentifier("btnClear","id",getActivity().getPackageName());
        int btnCloseId = view.getResources().getIdentifier("btnClose","id",getActivity().getPackageName());

        this.mTextView = ((TextView) view.findViewById(txtConsoleId));

        final Button btnClear = (Button) view.findViewById(btnClearId);
        final Button btnClose = (Button) view.findViewById(btnCloseId);

        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                OSConsoleFragment.this.clear();
                if (OSConsoleFragment.this.mListener != null) {
                    OSConsoleFragment.this.mListener.onClear();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (OSConsoleFragment.this.mListener != null) {
                    OSConsoleFragment.this.mListener.onClose();
                }
            }
        });
        return view;
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onHiddenChanged(boolean paramBoolean) {
        super.onHiddenChanged(paramBoolean);
    }

    public void onResume() {
        super.onResume();
        if (this.mListener != null) {
            this.mListener.onReadyToReceiveData();
        }
    }

    public void setOsUiConsoleListener(OSUiConsoleInterface paramOSUiConsoleInterface) {
        this.mListener = paramOSUiConsoleInterface;
    }

    public interface OSUiConsoleInterface {
        void onClear();
        void onClose();
        void onReadyToReceiveData();
    }
}