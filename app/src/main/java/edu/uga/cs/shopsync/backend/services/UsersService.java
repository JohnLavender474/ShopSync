package edu.uga.cs.shopsync.backend.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.uga.cs.shopsync.backend.exceptions.IllegalNullValueException;
import edu.uga.cs.shopsync.backend.exceptions.TaskFailureException;
import edu.uga.cs.shopsync.backend.exceptions.UserAlreadyExistsException;
import edu.uga.cs.shopsync.backend.firebase.UserShopSyncMapFirebaseReference;
import edu.uga.cs.shopsync.backend.firebase.UsersFirebaseReference;
import edu.uga.cs.shopsync.backend.models.UserProfileModel;
import edu.uga.cs.shopsync.utils.ErrorHandle;

/**
 * Service class for users.
 */
@Singleton
public class UsersService {

    private static final String TAG = "UsersService";

    private final UsersFirebaseReference usersFirebaseReference;
    private final UserShopSyncMapFirebaseReference userShopSyncMapFirebaseReference;

    @Inject
    public UsersService(@NonNull UsersFirebaseReference usersFirebaseReference,
                        @NonNull UserShopSyncMapFirebaseReference userShopSyncMapFirebaseReference) {
        this.usersFirebaseReference = usersFirebaseReference;
        this.userShopSyncMapFirebaseReference = userShopSyncMapFirebaseReference;
        Log.d(TAG, "UsersService: created");
    }

    /**
     * Adds a value event listener.
     *
     * @param valueEventListener the value event listener.
     */
    public void addValueEventListener(@NonNull ValueEventListener valueEventListener) {
        usersFirebaseReference.addValueEventListener(valueEventListener);
    }

    /**
     * Attempts to create a new user and add a user profile model to the user_profiles collection.
     * If the task succeeds, the user is automatically signed in and the onSuccess runnable is
     * run. If the task fails, the onFailure runnable is run. If either runnable is null, it is
     * not run.
     *
     * @param email    the user's email address
     * @param username the user's username
     * @param password the user's password
     */
    public void createUser(@NonNull String email, @NonNull String username,
                           @NonNull String password,
                           @Nullable Consumer<UserProfileModel> onSuccess,
                           @Nullable Consumer<ErrorHandle> onError)
            throws TaskFailureException, UserAlreadyExistsException, IllegalNullValueException {
        usersFirebaseReference.createUser(email, username, password, onSuccess, onError);
    }

    /**
     * Attempts to sign in the user with the given email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     */
    public @NonNull Task<AuthResult> signInUser(@NonNull String email, @NonNull String password) {
        return usersFirebaseReference.signInUser(email, password);
    }

    /**
     * Attempts to sign out the current user.
     */
    public void signOut() {
        usersFirebaseReference.signOut();
    }

    /**
     * Attempts to change the user's password.
     *
     * @param oldPassword              the user's old password
     * @param newPassword              the user's new password
     * @param onUpdatePasswordListener the listener for when the update password task completes
     * @param onFailureToAuthenticate  the listener for if the authentication task fails
     */
    public void changeUserPassword(@NonNull String oldPassword, @NonNull String newPassword,
                                   @Nullable Runnable onUpdatePasswordListener,
                                   @Nullable Runnable onFailureToAuthenticate) {
        usersFirebaseReference.changeUserPassword(oldPassword, newPassword,
                                                  onUpdatePasswordListener,
                                                  onFailureToAuthenticate);
    }

    /**
     * Returns true if the current user is signed in.
     *
     * @return true if the current user is signed in
     */
    public boolean isCurrentUserSignedIn() {
        return usersFirebaseReference.isCurrentUserSignedIn();
    }

    /**
     * Returns the task that fetches the user profile for the user with the given unique id.
     *
     * @param userUid the user's unique id.
     * @return the task that fetches the user profile.
     */
    public @NonNull Task<DataSnapshot> getUserProfileWithUid(@NonNull String userUid) {
        return usersFirebaseReference.getUserProfileWithUid(userUid);
    }

    /**
     * Returns the task that fetches the user profile for the user with the given email.
     *
     * @param email the user's email.
     * @return the task that fetches the user profile.
     */
    public @NonNull Task<DataSnapshot> getUserProfilesWithEmail(@NonNull String email) {
        return usersFirebaseReference.getUserProfileWithEmail(email);
    }

    /**
     * Returns the task that fetches the user profile of the current user. Returns null if the
     * current user is not signed
     * in.
     *
     * @return the task that fetches the user profile of the current user.
     */
    public @Nullable Task<DataSnapshot> getCurrentUserProfile() {
        return usersFirebaseReference.getCurrentUserProfile();
    }

    /**
     * Gets the current firebase user. Returns null if the current user is not signed in.
     *
     * @return the current firebase user.
     */
    public @Nullable FirebaseUser getCurrentFirebaseUser() {
        return usersFirebaseReference.getCurrentFirebaseUser();
    }

    /**
     * Attempts to delete the current user. Throws an exception if the user is not signed in.
     *
     * @param password  the user's password
     * @param onSuccess the runnable for if the task succeeds
     * @param onFailure the runnable for if the task fails
     */
    public void deleteCurrentUser(@NonNull String password, @Nullable Runnable onSuccess,
                                  @Nullable Runnable onFailure) {
        String userUid = usersFirebaseReference.deleteCurrentUser(password, onSuccess, onFailure);
        userShopSyncMapFirebaseReference.removeUser(userUid);
    }
}
