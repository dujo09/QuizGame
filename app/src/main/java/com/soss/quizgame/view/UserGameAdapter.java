package com.soss.quizgame.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.soss.quizgame.R;
import com.soss.quizgame.model.UserGame;

public class UserGameAdapter extends ListAdapter<UserGame, UserGameAdapter.UserGameVH> {

  private static final DiffUtil.ItemCallback<UserGame> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<UserGame>() {
        @Override
        public boolean areItemsTheSame(@NonNull UserGame oldItem, @NonNull UserGame newItem) {
          return oldItem.uid.equals(newItem.uid)
              && oldItem.timestamp.equals(newItem.timestamp);
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserGame oldItem, @NonNull UserGame newItem) {
          return oldItem.uid.equals(newItem.uid)
              && oldItem.score == newItem.score
              && oldItem.timestamp.equals(newItem.timestamp);
        }
      };

  public UserGameAdapter() {
    super(DIFF_CALLBACK);
  }

  @NonNull
  @Override
  public UserGameVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_games, parent, false);
    return new UserGameVH(v);
  }

  @Override
  public void onBindViewHolder(@NonNull UserGameVH holder, int position) {
    UserGame userGame = getItem(position);
    holder.bind(userGame);
  }

  public interface OnItemClick {
    void onClick(UserGame userGame);
  }

  public class UserGameVH extends RecyclerView.ViewHolder {
    TextView tvScore, tvTimestamp;

    UserGameVH(@NonNull View itemView) {
      super(itemView);
      tvScore = itemView.findViewById(R.id.tvScore);
      tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
    }

    void bind(UserGame userGame) {
      tvScore.setText(userGame.score + " \uD83C\uDF1F");
      tvTimestamp.setText(userGame.timestamp);
    }
  }
}
