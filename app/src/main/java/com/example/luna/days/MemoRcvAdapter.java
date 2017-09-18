package com.example.luna.days;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by LUNA on 2017-09-13.
 */

public class MemoRcvAdapter extends RecyclerView.Adapter{


    public static final int TEXT_TYPE = 0;
    public static final int AUDIO_TYPE = 1;
    public static final int TOTAL_TYPES = 2;

    private boolean fabStateVolume = false; //(오디오 그냥 메인에서 플레이할 수 있게?)
    public ArrayList<Item_memo> item_memoList;

    public int selected_position = 0; // You have to set this globally in the Adapter class

    RecyclerView recyclerView;
    private Context context;

    //Provide a suitable constructor (depends on the kind of dataset)
    public MemoRcvAdapter(ArrayList<Item_memo> item_memoList, Context context, RecyclerView recyclerView)
    {
        this.item_memoList = item_memoList;
        this.context = context;
        this.recyclerView = recyclerView;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemViewType(int position) {

        return item_memoList.get(position).getType();
    }



    @Override
    public int getItemCount() {
        return this.item_memoList.size();
    }


    public Object getItem(int position) {
        return item_memoList.get(position);
    }





    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    //일단 텍스트 메모만
    public class TextTypeViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
    { // each data item is just a string in this case

        public TextView tv_mainmemo1, tv_mainmemo2;
        public RelativeLayout memo_layout1;


        //ViewHolder 클래스는 RecyclerView의 item에 들어갈 view를 받은후 그 view 안에 있는 ImageView와 TextView를 초기화]
        public TextTypeViewHolder(View view)
        {
            super(view);
            //TODO super가 뭐야?
            memo_layout1 = (RelativeLayout) view.findViewById(R.id.memo_layout1);
            tv_mainmemo1 = (TextView) view.findViewById(R.id.tv_mainmemo1);
            tv_mainmemo2 = (TextView) view.findViewById(R.id.tv_mainmemo2);

            context = view.getContext();

            memo_layout1.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();


            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;

            // Updating old as well as new positions
            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);


            //수정 액티비티 소환
            Intent modifyTxtMemoIntent = new Intent(context.getApplicationContext(), ModifyTxtMemoActivity.class);

            //modifyTxtMemoIntent.putExtra("memo", item_memoList.get(selected_position));
            modifyTxtMemoIntent.putExtra("title", item_memoList.get(selected_position).getMemo1().toString());
            modifyTxtMemoIntent.putExtra("note", item_memoList.get(selected_position).getMemo2().toString());
            modifyTxtMemoIntent.putExtra("arrayIndex",selected_position);
            //계속 item_memoList.get(index)를 하는 바람에 데이터가 주고받는 게 오류가 났음..이하 주석처리한 애들도 시도했다가 실패
            // modifyTxtMemoIntent.putExtra("title", item_memoList.get(index).getMemo1().toString());
            // modifyTxtMemoIntent.putExtra("note", item_memoList.get(index).getMemo2().toString());
            //TODO selected_position = getAdapterPosition();


            //수정 액티비티로 데이터 보내기
            ((Activity) context). startActivityForResult(modifyTxtMemoIntent, MainActivity.REQUEST_MODIFY_TXTMEMO);
        }//onClick
    }//TextTypeViewHolder


    public class AudioTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tv1_mainmemo_record, tv2_mainmemo_record;
        Button fakePlayBtn;
        public RelativeLayout memo_layout_record;

        public AudioTypeViewHolder(View view)
        {
            super(view);
            this.tv1_mainmemo_record = (TextView) view.findViewById(R.id.tv1_mainmemo_record);
            this.tv2_mainmemo_record = (TextView) view.findViewById(R.id.tv2_mainmemo_record);
            this.fakePlayBtn = (Button) view.findViewById(R.id.fakePlayBtn);

            this.memo_layout_record = (RelativeLayout) view.findViewById(R.id.memo_layout_record);
            context = view.getContext();

            memo_layout_record.setOnClickListener(this);

        }//AudioTypeViewHolder

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();

            if (getAdapterPosition() == RecyclerView.NO_POSITION)
                return;

            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);

            //전달할 것, 텍스트메모+오디오메모+seekbar정보
            Intent modifyAudioMemoIntent = new Intent(context.getApplicationContext(), ModifyAudioMemoActivity.class);
            modifyAudioMemoIntent.putExtra("title", item_memoList.get(selected_position).getMemo1());
            modifyAudioMemoIntent.putExtra("note", item_memoList.get(selected_position).getMemo2());

            //Uri라고 써있지만 실은 String값임
            modifyAudioMemoIntent.putExtra("audioUri", item_memoList.get(selected_position).getAudioUri());
            Log.e("메인꺼 AudioURI",item_memoList.get(selected_position).getAudioUri());
            modifyAudioMemoIntent.putExtra("arrayIndex", selected_position);

            ((Activity) context).startActivityForResult(modifyAudioMemoIntent, MainActivity.REQUEST_MODIFY_AUIDOMEMO);

        }
    }//AudioTypeViewHolder


    // Create new views (invoked by the layout manager)
    //onCreateViewHolder 메서드안의 내용을 잘 바꾸어야 내가 원하는 결과물이 나온다.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view;
        switch(viewType)
        {
            case TEXT_TYPE:
                view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_memo, parent, false);
                return new TextTypeViewHolder(view);

            case AUDIO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_record, parent, false);
                return new AudioTypeViewHolder(view);
        }//switch
        /*View txtMemoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_memo, parent, false);
        TextTypeViewHolder memoViewHolder = new TextTypeViewHolder(txtMemoView);
*/
        return null;
    }


    // Replace the contents of a view (invoked by the layout manager)
    //RecyclerView의 item의 셋팅과 사용자가 스크롤링 할때, 호출되는 메서드
    //우리가 원하는 데이터가 포지션별로 ArrayList<MyData>에 저장되어 있다. 이러한 데이터를 포지션별로 보여주는 것을 보장해준다.
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        //하단-> 지저분해서 안 쓸 예정
        // holder.memo_layout1.setBackgroundColor(selected_position == position ? Color.LTGRAY : Color.TRANSPARENT);


        //멀티플 뷰홀더 시도 전 원래 있던 애
/*        TextTypeViewHolder.tv_mainmemo1.setText(item_memoList.get(position).memo1);
        TextTypeViewHolder.tv_mainmemo2.setText(item_memoList.get(position).memo2);*/

        Item_memo item_memo = item_memoList.get(position);

        if (item_memo != null)
        {
            switch (item_memo.getType())
            {
                case TEXT_TYPE:

                    ((TextTypeViewHolder) holder).tv_mainmemo1.setText(item_memoList.get(position).memo1);
                    ((TextTypeViewHolder) holder).tv_mainmemo2.setText(item_memoList.get(position).memo2);

                    Log.e("", "제목길이:"+ ((TextTypeViewHolder) holder).tv_mainmemo1.getText().length() );
                    Log.e("", "내용길이:"+ ((TextTypeViewHolder) holder).tv_mainmemo2.getText().length() );
                    Log.e("", "제목:"+ ((TextTypeViewHolder) holder).tv_mainmemo1.getText());
                    Log.e("", "내용:"+ ((TextTypeViewHolder) holder).tv_mainmemo2.getText());

/*                    if(  ((TextTypeViewHolder) holder).tv_mainmemo1.getText().length() <= 0 && ((TextTypeViewHolder) holder).tv_mainmemo2.getText().length() > 0)
                    {

                        ((TextTypeViewHolder) holder).tv_mainmemo2.setText(item_memoList.get(position).memo2);
                        ((TextTypeViewHolder) holder).tv_mainmemo1.setVisibility(View.GONE);
                        ((TextTypeViewHolder) holder).tv_mainmemo2.setVisibility(View.VISIBLE);
                    }

                   if(   ((TextTypeViewHolder) holder).tv_mainmemo1.getText().length() > 0 &&((TextTypeViewHolder) holder).tv_mainmemo2.getText().length() == 0 )
                    {
                        ((TextTypeViewHolder) holder).tv_mainmemo1.setText(item_memoList.get(position).memo1);
                        ((TextTypeViewHolder) holder).tv_mainmemo1.setVisibility(View.VISIBLE);
                        ((TextTypeViewHolder) holder).tv_mainmemo2.setVisibility(View.GONE);
                    }
                    if(  ((TextTypeViewHolder) holder).tv_mainmemo1.getText().length() > 0 && ((TextTypeViewHolder) holder).tv_mainmemo2.getText().length() > 0)
                    {
                        ((TextTypeViewHolder) holder).tv_mainmemo1.setText(item_memoList.get(position).memo1);
                        ((TextTypeViewHolder) holder).tv_mainmemo2.setText(item_memoList.get(position).memo2);
                        ((TextTypeViewHolder) holder).tv_mainmemo1.setVisibility(View.VISIBLE);
                        ((TextTypeViewHolder) holder).tv_mainmemo2.setVisibility(View.VISIBLE);
                    }*/
                    break;

                case AUDIO_TYPE:
                    ((AudioTypeViewHolder) holder).tv1_mainmemo_record.setText(item_memoList.get(position).memo1);
                    ((AudioTypeViewHolder) holder).tv2_mainmemo_record.setText(item_memoList.get(position).memo2);
                    ((AudioTypeViewHolder) holder).fakePlayBtn.setBackgroundResource(R.drawable.simple_play_btn_24px);
                    break;

            }//switch
        }// if (item_memo != null)
    }


    public void addItem(Item_memo item_memo)
    {
        item_memoList.add(item_memo);
        item_memo.setType(TEXT_TYPE);
    }

    //오디오 스타일 메모 추가
    public void addItem(String fileName, String title, String note)
    {
        Item_memo item_memo = new Item_memo(fileName, title, note);
        item_memo.setType(AUDIO_TYPE);
        item_memo.setMemo1(title);
        item_memo.setMemo2(note);
        item_memo.setAudioUri(fileName);
        item_memoList.add(item_memo);
    }



    //메모 삭제
    public void remove(int position) {
        item_memoList.remove(position);
        notifyItemRemoved(position);
    }

}
