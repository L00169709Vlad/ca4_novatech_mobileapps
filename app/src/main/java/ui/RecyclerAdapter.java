package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorryaplicationproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.MemoryObj;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private List<MemoryObj> memoryObjList;

    public RecyclerAdapter(Context context, List<MemoryObj> memoryObjList) {
        this.context = context;
        this.memoryObjList = memoryObjList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.memory_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder viewHolder, int position) {

        MemoryObj memoryObj = memoryObjList.get(position);
        String imageUrl;

        viewHolder.title.setText(memoryObj.getTitle());
        viewHolder.thoughts.setText(memoryObj.getDescription());
        //viewHolder.name.setText(memoryObj.getUserName());
        imageUrl = memoryObj.getImageUrl();
        /*
        minutes ago...
        Source https://medium.com/@shaktisinh/time-a-go-in-android-8bad8b171f87
        */
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(memoryObj.
                getTimeAdded().
                getSeconds() * 1000);

        viewHolder.dateAdded.setText(timeAgo);


        /*
        Use Picasso library to download and show image
         */
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.button_1)
                .fit()
                .into(viewHolder.image);


    }

    @Override
    public int getItemCount() {
        return memoryObjList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView
                title,
                thoughts,
                dateAdded,
                name;
        public ImageView image;
        String userId;
        String username;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;
            title=itemView.findViewById(R.id.memory_title_list);
            thoughts = itemView.findViewById(R.id.memory_memory_list);
            dateAdded = itemView.findViewById(R.id.memory_timestamp_list);
            image = itemView.findViewById(R.id.memory_image_list);
           // name = itemView.findViewById(R.id.)

        }
    }
}
