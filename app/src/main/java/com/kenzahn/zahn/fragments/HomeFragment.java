package com.kenzahn.zahn.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kenzahn.zahn.R;
import com.kenzahn.zahn.interfaces.CallBackListener;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3Q = "param3";
    private static final String ARG_PARAM4F = "param4";
    private static final String ARG_PARAM5P = "param5";
    /* access modifiers changed from: private */
    public CallBackListener callBackListener;
    private String isFlash;
    private String isPodcast;
    private String isQa;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2, String isQa2, String isFlash2, String isPodcast2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3Q, isQa2);
        args.putString(ARG_PARAM4F, isFlash2);
        args.putString(ARG_PARAM5P, isPodcast2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ImageView iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
        iv_menu.setVisibility(View.VISIBLE);
        LinearLayout card_flash = (LinearLayout) view.findViewById(R.id.card_flash);
        LinearLayout card_qa = (LinearLayout) view.findViewById(R.id.card_qa);
        LinearLayout glossary = (LinearLayout) view.findViewById(R.id.glossary);
        LinearLayout podcast = (LinearLayout) view.findViewById(R.id.podcast);
        LinearLayout acronys = (LinearLayout) view.findViewById(R.id.acronys);
        LinearLayout ll_both = (LinearLayout) view.findViewById(R.id.ll_both);
        TypeFaceTextViewBold tv_home_name = (TypeFaceTextViewBold) view.findViewById(R.id.tv_home_name);
        tv_home_name.setMaxLines(1);
        tv_home_name.setText("" + this.mParam1);
        if (this.isQa.equalsIgnoreCase("yes")) {
            card_qa.setVisibility(View.VISIBLE);
        } else {
            card_qa.setVisibility(View.GONE);
        }
        if (this.isFlash.equalsIgnoreCase("yes")) {
            card_flash.setVisibility(View.VISIBLE);
        } else {
            card_flash.setVisibility(View.GONE);
        }
        if (this.mParam2.equalsIgnoreCase("yes")) {
            ll_both.setVisibility(View.VISIBLE);
        } else {
            ll_both.setVisibility(View.GONE);
        }
        if (!this.isFlash.equalsIgnoreCase("no") || !this.isQa.equalsIgnoreCase("no")) {
            ll_both.setVisibility(View.VISIBLE);
        } else {
            ll_both.setVisibility(View.GONE);
        }
        if (this.isPodcast.equalsIgnoreCase("yes")) {
            podcast.setVisibility(View.VISIBLE);
        } else {
            podcast.setVisibility(View.GONE);
        }
        iv_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HomeFragment.this.callBackListener != null) {
                    HomeFragment.this.callBackListener.onCallBack("open");
                }
            }
        });
        card_flash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HomeFragment.this.callBackListener != null) {
                    iv_menu.setVisibility(View.GONE);
                    HomeFragment.this.callBackListener.onCallBack("flash");
                }
            }
        });
        glossary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HomeFragment.this.callBackListener != null) {
                    HomeFragment.this.callBackListener.onCallBack("glossary");
                }
            }
        });
        acronys.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HomeFragment.this.callBackListener != null) {
                    HomeFragment.this.callBackListener.onCallBack("acronys");
                }
            }
        });
        card_qa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HomeFragment.this.callBackListener != null) {
                    HomeFragment.this.callBackListener.onCallBack("qa");
                }
            }
        });
        podcast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HomeFragment.this.callBackListener != null) {
                    HomeFragment.this.callBackListener.onCallBack("pod");
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity() is fully created in onActivityCreated and instanceOf differentiate it between different Activities
        if (getActivity() instanceof CallBackListener)
            callBackListener = (CallBackListener) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
            this.isQa = getArguments().getString(ARG_PARAM3Q);
            this.isFlash = getArguments().getString(ARG_PARAM4F);
            this.isPodcast = getArguments().getString(ARG_PARAM5P);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);

        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
