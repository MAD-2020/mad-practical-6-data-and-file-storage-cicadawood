package sg.edu.np.week_6_whackamole_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    UserData user;
    ArrayList<Integer> levelList;
    ArrayList<Integer> scoreList;

    public CustomScoreAdaptor(UserData userdata){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        this.user = userdata;
        this.levelList = user.getLevels();
        this.scoreList = user.getScores();
    }

    @NonNull
    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        /* Hint:
        This method dictates how the viewholder layuout is to be once the viewholder is created.
         */
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select,parent,false);
        return new CustomScoreViewHolder(item);
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */
        final int levelNo = levelList.get(position);
        final int score = scoreList.get(position);
        holder.levelNoTxtView.setText("Level " + String.valueOf(levelNo));
        holder.highestScoreTxtView.setText("Highest Score: "+String.valueOf(score));
        Log.v(TAG, FILENAME + " Showing level " + levelNo + " with highest score: " + score);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(),GameActivity.class);
                intent.putExtra("username",user.getMyUserName());
                intent.putExtra("level",levelNo);
                intent.putExtra("score",score);
                v.getContext().startActivity(intent);
                Log.v(TAG, FILENAME+ ": Load level " + (position+1) +" for: " + user.getMyUserName());

            }
        });




    }



    @Override
    public int getItemCount() {
        /* Hint:
        This method returns the the size of the overall data.
         */
        return levelList.size();


    }

}