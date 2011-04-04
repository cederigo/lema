package ch.unibe.lema.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.unibe.lema.R;
import ch.unibe.lema.provider.Lecture;

/**
 * representation for one list item.
 * 
 * @author cede
 *
 */

public class LectureListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Lecture> lectureList;
    
    static class ViewHolder {
        TextView title;
        TextView details;
        ImageView icon;
    }
 
    
    public LectureListAdapter(
            Context ctx,
            List<Lecture> lectureList) {
        
        this.lectureList = lectureList;
        inflater = LayoutInflater.from(ctx);
    }
    
    
    public int getCount() {
        return lectureList.size();
    }

    public Object getItem(int position){
        return position;
    }
    
    /**
     * use list-index as unique id.
     */    
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        
        ViewHolder holder;
        
        if ( convertView == null ){
            convertView = inflater.inflate(R.layout.lecturelist_item, null);
            View subView = convertView.findViewById(R.id.lecturelistitem_sublayout);
            
            holder = new ViewHolder();
            holder.title = (TextView)subView.findViewById(R.id.lecturelistitem_title);
            holder.details = (TextView)subView.findViewById(R.id.lecturelistitem_details);
            holder.icon = (ImageView)convertView.findViewById(R.id.lecturelistitem_icon);
            
            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        Lecture l = lectureList.get(position);
        
        holder.title.setText(l.getTitle());
        holder.details.setText("some details");
        //TODO turn on/off based on subscription status
        holder.icon.setImageResource(android.R.drawable.star_big_off);
        
        return convertView;
    }
    
    public void add(Lecture l) {
        lectureList.add(l);
    }
    
    public void clear(){
        lectureList.clear();
    }

}
