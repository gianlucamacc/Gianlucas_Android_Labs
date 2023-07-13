package algonquin.cst2335.macc0112;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.macc0112.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.macc0112.databinding.SentMessageBinding;
import algonquin.cst2335.macc0112.ui.data.ChatRoomViewModel;


public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel ;
    ArrayList<String> messages = new ArrayList<>();
    private RecyclerView.Adapter myAdapter;


    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView time;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if(messages == null)
            {
            chatModel.messages.postValue( messages = new ArrayList<String>());
            }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_chat_room);




        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
        @NonNull
        @Override
        public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater());
            return new MyRowHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            String obj = messages.get(position);
            holder.message.setText(obj);
            holder.time.setText("");
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }
        @Override
        public int getItemViewType(int position){
            return 0;
        }
    };
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.sendButton.setOnClickListener(click -> {
            messages.add(binding.textInput.getText().toString());
            myAdapter.notifyItemInserted(messages.size()-1);

            binding.textInput.setText("");
        });

    }
}