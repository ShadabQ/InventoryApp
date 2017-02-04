/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.inventory;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventory.data.ProductContract.ProductEntry;

import static com.example.android.inventory.R.id.saleButton;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {


    private static final String LOG_TAG = ProductCursorAdapter.class.getSimpleName();
    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        View view   =   LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        ViewHolderItem viewHolderItem=new ViewHolderItem();
        viewHolderItem.nameTextView = (TextView) view.findViewById(R.id.name);
        viewHolderItem.priceTextView = (TextView) view.findViewById(R.id.price);
        viewHolderItem.qtyTextView = (TextView) view.findViewById(R.id.quantity);
        viewHolderItem.imgView=(ImageView)view.findViewById(R.id.product_image);

        viewHolderItem.saleButton = (Button)view.findViewById(R.id.saleButton);
        viewHolderItem.incrementQuantity=(Button)view.findViewById(R.id.increment_quantity);
        viewHolderItem.decrementQuantity    =   (Button)view.findViewById(R.id.decrement_quantity);
        view.setTag(viewHolderItem);
        return view;
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout

        ViewHolderItem holder=(ViewHolderItem) view.getTag();

                // Find the columns of product attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int qtyColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int imgColumnIndex=cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_IMAGE_URI);
        int productId =   cursor.getColumnIndex(ProductEntry._ID);
        // Read the pet attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        int prodPrice = cursor.getInt(priceColumnIndex);
        int qty=cursor.getInt(qtyColumnIndex);
        String uri=cursor.getString(imgColumnIndex);

        holder.imgView.setImageURI(Uri.parse(uri));
        holder.qtyTextView.setText(Integer.toString(qty));
        // Update the TextViews with the attributes for the current pet
        holder.nameTextView.setText(productName);
        holder.priceTextView.setText(Integer.toString(prodPrice));

        final int quantityUpdate = qty;
        final long itemId = cursor.getLong(productId);
        holder.saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = quantityUpdate;
                if (qty > 0) {
                    qty--;
                }
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY,
                        qty);
                ContentResolver contentResolver = v.getContext().getContentResolver();
                Uri uri = ContentUris.withAppendedId(
                        ProductEntry.CONTENT_URI,
                        itemId);
                contentResolver.update(uri, values, null, null);
            }
        });
        holder.decrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = quantityUpdate;
                if (qty > 0) {
                    qty--;
                }
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY,
                        qty);
                ContentResolver contentResolver = v.getContext().getContentResolver();
                Uri uri = ContentUris.withAppendedId(
                        ProductEntry.CONTENT_URI,
                        itemId);
                contentResolver.update(uri, values, null, null);
            }
        });
        holder.incrementQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = quantityUpdate;
                qty++;
                ContentValues values = new ContentValues();
                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY,
                        qty);
                ContentResolver contentResolver = v.getContext().getContentResolver();
                Uri uri = ContentUris.withAppendedId(
                        ProductEntry.CONTENT_URI,
                        itemId);
                contentResolver.update(uri, values, null, null);
            }
        });
    }


    static class ViewHolderItem{
        TextView nameTextView;
        TextView priceTextView;
        TextView qtyTextView;
        ImageView imgView;
        Button saleButton;
        Button incrementQuantity;
        Button decrementQuantity;
    }

}
