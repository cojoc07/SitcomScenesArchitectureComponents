package com.flukeapps.notesarchcomponents.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.AsyncTask;
import android.widget.Toast;

import com.flukeapps.notesarchcomponents.model.Scene;
import com.flukeapps.notesarchcomponents.retrofit.RetrofitApi;
import com.flukeapps.notesarchcomponents.retrofit.RetrofitClient;
import com.flukeapps.notesarchcomponents.room.SceneDAO;
import com.flukeapps.notesarchcomponents.room.SceneDatabase;
import com.flukeapps.notesarchcomponents.utils.Utils;

import java.util.List;

public class SceneRepository {

    private SceneDAO sceneDAO;
    private LiveData<List<Scene>> allScenes;
    private Scene sceneToReturn;

    public SceneRepository(Application application){
        SceneDatabase database = SceneDatabase.getInstance(application);

        sceneDAO = database.sceneDAO();
        allScenes = sceneDAO.getAllScenes();
    }

    public LiveData<List<Scene>> getAllScenes() {
        return allScenes;
    }

    public void InsertScene_fetchedOrGenerated(){
        Retrofit retrofit = RetrofitClient.getClient();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<List<Scene>> call = retrofitApi.getWebNotes();

        call.enqueue(new Callback<List<Scene>>() {
            @Override
            public void onResponse(Call<List<Scene>> call, Response<List<Scene>> response) {
                System.out.println("============= LOADING FROM WEB");
                List<Scene> scenes = response.body();
                sceneToReturn = scenes.get(0);
                insert(sceneToReturn);
            }

            @Override
            public void onFailure(Call<List<Scene>> call, Throwable t) {
                System.out.println("============= LOADING LOCALLY");
                System.out.println("============= " + t.getMessage());
                sceneToReturn = Utils.generateRandomScene();
                insert(sceneToReturn);
            }
        });
    }

    public void insert(Scene scene){
        new InsertSceneAsyncTask(sceneDAO).execute(scene);
    }

    public void update(Scene scene){
        new UpdateSceneAsyncTask(sceneDAO).execute(scene);
    }

    public void delete(Scene scene){
        new DeleteSceneAsyncTask(sceneDAO).execute(scene);
    }

    public void deleteAllScenes(){
        new DeleteAllScenesAsyncTask(sceneDAO).execute();
    }

    private static class InsertSceneAsyncTask extends AsyncTask<Scene, Void, Void> {
        private SceneDAO sceneDao;

        private InsertSceneAsyncTask(SceneDAO sceneDao) {
            this.sceneDao = sceneDao;
        }

        @Override
        protected Void doInBackground(Scene... scenes) {
            sceneDao.insert(scenes[0]);
            return null;
        }
    }

    private static class UpdateSceneAsyncTask extends AsyncTask<Scene, Void, Void> {
        private SceneDAO sceneDao;

        private UpdateSceneAsyncTask(SceneDAO sceneDao) {
            this.sceneDao = sceneDao;
        }

        @Override
        protected Void doInBackground(Scene... scenes) {
            sceneDao.update(scenes[0]);
            return null;
        }
    }

    private static class DeleteSceneAsyncTask extends AsyncTask<Scene, Void, Void> {
        private SceneDAO sceneDao;

        private DeleteSceneAsyncTask(SceneDAO sceneDao) {
            this.sceneDao = sceneDao;
        }

        @Override
        protected Void doInBackground(Scene... scenes) {
            sceneDao.delete(scenes[0]);
            return null;
        }
    }

    private static class DeleteAllScenesAsyncTask extends AsyncTask<Void, Void, Void> {
        private SceneDAO sceneDao;

        private DeleteAllScenesAsyncTask(SceneDAO sceneDao) {
            this.sceneDao = sceneDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sceneDao.deleteAllScenes();
            return null;
        }
    }
}

