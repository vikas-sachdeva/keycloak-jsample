##Prerequisites

1. Create realm in keycloak server
2. Create first client application with Access Type *public*
3. Create second client application with Access Type *bearer-only*
4. Create 2 roles *user* and *admin*
5. Create first user with role *user*
6. Create second user with role *admin*

##Execution Steps

1. Admin will have access on all APIs including admin API

2. User will have access on all APIs excluding admin API 

3. Get admin access_token using client with Access Type *public*  -

    curl -X POST \
        https://<keycloakIp>:8443/auth/realms/<realmName>/protocol/openid-connect/token \
        -H 'Content-Type: application/x-www-form-urlencoded' \
        -H 'cache-control: no-cache' \
        -d 'grant_type=password&client_id=<clientId>&username=<adminUserName>&password=<password'

e.g.

    curl -X POST \
        https://192.168.56.101:8443/auth/realms/jsample/protocol/openid-connect/token \
        -H 'Content-Type: application/x-www-form-urlencoded' \
        -H 'cache-control: no-cache' \
        -d 'grant_type=password&client_id=spring-boot-public&username=rootuser&password=rootuser'

4. Set access_token into *Authorization* header and call admin API or user API -

    curl -X GET \
        http://localhost:8080/admin/hello \
        -H 'Authorization: <accessToken>' \
        -H 'Content-Type: application/x-www-form-urlencoded' \
        -H 'cache-control: no-cache' 

e.g. 

    curl -X GET \
        http://localhost:8080/admin/hello \
        -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrd2pyUERHaEc3UjlfNERwWVhOUXJwWGVfbkZFc0JKOVA5a2tkQlQ5ZzEwIn0.eyJqdGkiOiIwYTMxOTMwMC01ZGRjLTQzNTEtYTg4OS1kNDg5NDM1ZWQ0YWYiLCJleHAiOjE1NDk0NTIzMjQsIm5iZiI6MCwiaWF0IjoxNTQ5NDUyMDI0LCJpc3MiOiJodHRwOi8vMTkyLjE2OC41Ni4xMDE6ODA4MC9hdXRoL3JlYWxtcy9qc2FtcGxlIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImM1ZTNkZjllLTk4ZGUtNGRmNy1hYzhiLTZhNWMzOGMyNWU5YyIsInR5cCI6IkJlYXJlciIsImF6cCI6InNwcmluZy1ib290LXB1YmxpYyIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6IjFhYTVjNTRjLTYyNzEtNDM1MS1hOGEyLWIyZTc0NDdhNzZkZCIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJhZG1pbiIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoicm9vdHVzZXIgcm9vdHVzZXIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJyb290dXNlciIsImdpdmVuX25hbWUiOiJyb290dXNlciIsImZhbWlseV9uYW1lIjoicm9vdHVzZXIiLCJlbWFpbCI6InJvb3R1c2VyQHRlc3QuY29tIn0.DUadJ1diuVsxbwzO_S-ZOss5p2jgU9MBRGFqiFxrw2-6Wa6ZbvWU_aPZzRv8PPz9QR5m_tfkGH_yRow6c77-jzWjCkJM501YfB2oXfZlmkFwiSHoHphiefLbliOB5Te8kbEWcsPckUdPRnwOfjfLe-Ug37Wym-Fr_nxCR_ejLgPpf_7wkBgQVTjCTLXt7SbwKfvVIUCDpMvTyS5jEso4JbB4v-fa6EGk-GH7Z-DJsG9KcUFaq5IaRnWtIeOfWMq0_NJ6F8FQMYN-KN4EozszBZR-4zLS0bXcNwLCJWJQ_iQzZsfGHZM8MOrAkD2IpNa-3djLB9VIJBiJUlLrreUtqA' \
    -H 'Content-Type: application/x-www-form-urlencoded' \
    -H 'cache-control: no-cache'


5. Similarly, user API can be called.

**Note** This sample will not work when URL of the application is accessed through browser because client Access Type set in keycloak server is of "bearer-only" type.