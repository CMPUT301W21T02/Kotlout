package xyz.kotlout.kotlout.controller;

import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class PostController {

//  public static final String POSTS_COLLECTION = "posts";

  public PostController(String experimentID){
    FirebaseFirestore firestore = FirebaseController.getFirestore();

//    CollectionReference posts = firestore.collection(ExperimentController.EXPERIMENT_COLLECTION)
//        .document(experimentID)
//        .collection(POSTS_COLLECTION);

//    posts.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//      @Override
//      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//        queryDocumentSnapshots.toObjects()
//      }
//    }
//
//    posts.addSnapshotListener(new EventListener<QuerySnapshot>() {
//      @Override
//      public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//        value.
//      }
//    })
  }
}
