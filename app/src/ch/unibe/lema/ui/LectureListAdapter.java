package ch.unibe.lema.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.unibe.lema.R;
import ch.unibe.lema.Service;
import ch.unibe.lema.provider.Lecture;

/**
 * representation for one list item.
 * 
 * @author cede
 * 
 */

public class LectureListAdapter extends BaseAdapter {

    private static final String LOG_TAG = "LectureListAdapter";
    private LayoutInflater inflater;
    private List<Lecture> lectureList;
    private Context context;
    private Service service;

    static class ViewHolder {
        TextView title;
        TextView details;
        ImageView icon;
    }

    public LectureListAdapter(Context ctx, List<Lecture> lectureList,
            Service service) {

        this.lectureList = lectureList;
        inflater = LayoutInflater.from(ctx);
        this.context = ctx;
        this.service = service;
    }

    public int getCount() {
        return lectureList.size();
    }

    public Lecture getItem(int position) {
        return lectureList.get(position);
    }

    /**
     * use list-index as unique id.
     */
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lecturelist_item, null);
            View subView = convertView
                    .findViewById(R.id.lecturelistitem_sublayout);

            holder = new ViewHolder();
            holder.title = (TextView) subView
                    .findViewById(R.id.lecturelistitem_title);
            holder.details = (TextView) subView
                    .findViewById(R.id.lecturelistitem_details);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.lecturelistitem_icon);

            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Lecture l = lectureList.get(position);

        holder.title.setText(l.getTitle());
        holder.details.setText(l.getStaff());

        if (l.isSubscription()) {
            holder.icon.setImageResource(android.R.drawable.star_big_on);
        } else {
            holder.icon.setImageResource(android.R.drawable.star_big_off);
        }

        final int index = position;
        holder.icon.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO refactor duplicated code
                if (l.isSubscription()) {
                    Lecture replacement = service.unsubscribe(l);
                    lectureList.set(index, replacement);
                    notifyDataSetChanged();

                    service.showInfo("Unsubscribed from " + l.getTitle());
                    ImageView icon = (ImageView) v
                            .findViewById(R.id.lecturelistitem_icon);
                    icon.setImageResource(android.R.drawable.star_big_off);
                } else {
                    Lecture replacement = service.subscribe(l);
                    lectureList.set(index, replacement);
                    notifyDataSetChanged();

                    service.showInfo("Subscribed to " + l.getTitle());
                    ImageView icon = (ImageView) v
                            .findViewById(R.id.lecturelistitem_icon);
                    icon.setImageResource(android.R.drawable.star_big_on);
                }

            }
        });

        return convertView;
    }

    public void add(Lecture l) {
        lectureList.add(l);
    }

    public void clear() {
        lectureList.clear();
    }

}
