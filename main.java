
class Request{
    // Using post method to send binary stream to Microsoft Speech Recognition.
    private void post() {
        File file_wav = new File(get_directory() + "/voice16k16bitmono.wav");
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody requestBody = RequestBody.create(mediaType, file_wav);
            Request request = new Request.Builder()
                    .url("https://westus.api.cognitive.microsoft.com/spid/v1.0/identify?identificationProfileIds=7cfe0579-afea-4b8b-83ee-3c954fa767a3&shortAudio=true")
                    .post(requestBody)
                    .addHeader("Ocp-Apim-Subscription-Key", "373a48290e6f45f58c0cf80a5d884149")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "0731d91d-8239-41e4-9057-cf4e8fd02097")
                    .build();

            Response response = client.newCall(request).execute();
            String operation = response.header("Operation-location");
            //Do something with response..
            Log.d(TAG, "Get operation ID: "+ operation);

            // Call for second time
            Request request1 = new Request.Builder()
                    .url(operation)
                    .addHeader("Ocp-Apim-Subscription-Key", "373a48290e6f45f58c0cf80a5d884149")
                    .get()
                    .build();
            Operation operation1 = null;
            while (operation1 == null || !operation1.getStatus().equals("succeeded")){
                Response response1 = client.newCall(request1).execute();
                assert response1.body() != null;
                String result = response1.body().string();
                // Gson is required to parse Java to function
                Gson gson = new Gson();
                operation1 = gson.fromJson(result, Operation.class);
                Log.d(TAG, "post: " + operation1);
            }

            // Cannot identify
            if (operation1.getProcessingResult().getIdentifiedProfileId().equals("00000000-0000-0000-0000-000000000000")){
                Log.e(TAG, "post: The user is not authenticated");
            }
            else {
                // Do Something else
            }
        } catch (Exception e) {
            // show error
            Log.d(TAG, "post: "+ e);
        }
    }
}

