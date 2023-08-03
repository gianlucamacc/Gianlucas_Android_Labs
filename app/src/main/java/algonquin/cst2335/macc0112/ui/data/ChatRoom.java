package algonquin.cst2335.macc0112.ui.data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import algonquin.cst2335.macc0112.R;
import algonquin.cst2335.macc0112.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.macc0112.databinding.SentMessageBinding;
import algonquin.cst2335.macc0112.databinding.ReceiveMessageBinding;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.id_item1)
        {
            Toast.makeText(this, "You clicked on Delete", Toast.LENGTH_LONG).show();

        }
        else if (item.getItemId() == R.id.id_item2)
        {
            Toast.makeText(this, "Version 1.0, created by Gianluca Macchiagodena", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        ChatMessageDAO mDAO = db.cmDAO();





        public MyRowHolder(@NonNull View itemView) {
            super(itemView);



            itemView.setOnClickListener(clk -> {

                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = messages.get(position);

                chatmodel.selectedMessage.postValue(selected);



               AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                    .setTitle("Question")
                        .setNegativeButton("No", (dialog, cl)->{})
                    .setPositiveButton("Yes", (dialog, cl)->{
                    ChatMessage removedMessage = messages.get(position);

                    messages.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() ->{
                            mDAO.deleteMessages( removedMessage );

                            runOnUiThread(() ->{
                                Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG).
                                        setAction("Undo",c ->{
                                            messages.add(position, removedMessage);
                                            myAdapter.notifyItemInserted(position);
                                        }).show();
                            });
                        });

                    Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG).
                            setAction("Undo",c ->{
                        messages.add(position, removedMessage);
                        myAdapter.notifyItemInserted(position);
                    }).show();
                })
                .create().show();

            }
            );
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
        public void bind(ChatMessage chatMessage) {
            messageText.setText((chatMessage.getMessage()));
            timeText.setText(chatMessage.getTimeSent());
        }
    }


    ChatRoomViewModel chatmodel;

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy \nhh:mm:ss a");
        return sdf.format(new Date());
    }


    ArrayList<ChatMessage> messages = new ArrayList<>();

    private RecyclerView.Adapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {






        if (chatmodel == null) {
            chatmodel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);


        chatmodel.selectedMessage.observe(this, (newMessageValue) ->{
            MessageDetailsFragment chatFragment = new MessageDetailsFragment(newMessageValue);
            getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.fragmentLocation, chatFragment).commit();





        });

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        ChatMessageDAO mDAO = db.cmDAO();


        chatmodel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatmodel.messages.getValue();
        if(messages == null){
            chatmodel.messages.setValue(messages = new ArrayList<ChatMessage>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                messages.addAll(mDAO.getAllMessages());
                runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));

            });
        }

        binding.sendButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            String timeSent = getCurrentTime();
            boolean isSentButton = true;
            ChatMessage chatMessage = new ChatMessage(message, timeSent, isSentButton);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
            });
        });

        binding.receiveButton.setOnClickListener(click -> {
            String message = binding.textInput.getText().toString();
            String timeSent = getCurrentTime();
            boolean isSentButton = false;
            ChatMessage chatMessage = new ChatMessage(message, timeSent, isSentButton);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");


            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage);
            });
        });







        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(inflater, parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(inflater, parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.bind(chatMessage);

            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position){
                ChatMessage chatMessage = messages.get(position);
                if(chatMessage.isSentButton()){
                    return 0;
                }
                else{
                    return 1;
                }
            }
        });



    }
}