package com.example.front_grocery_android.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.front_grocery_android.models.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ListsLiveData extends LiveData<Lists> {
    private final ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Lists lists = snapshot.getValue(Lists.class);
            setValue(lists);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            System.out.println(error.toString());
        }
    };

    DatabaseReference databaseReference;

    public ListsLiveData(DatabaseReference ref) {
        databaseReference = ref;
    }

    @Override
    protected void onActive() {
        super.onActive();
        databaseReference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        databaseReference.removeEventListener(listener);
    }
}
