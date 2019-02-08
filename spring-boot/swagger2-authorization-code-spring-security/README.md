##Prerequisites

1. Create realm in keycloak server
2. Create first client application with Access Type *public*
3. For this client, set *Web Origins* to * in keycloak UI
3. Create second client application with Access Type *bearer-only*
4. Create 2 roles *user* and *admin*
5. Create first user with role *user*
6. Create second user with role *admin*


##Execution Steps

1. Admin will have access on all APIs including admin API

2. User will have access on all APIs excluding admin API 

3. Open *http://localhost:8080/swagger-ui.html* URL

4. Click on *Authorize* button. It will open one dialog with the details of keycloak client application.

5. Click on *Authorize* button on that dialog. Login screen of keycloak will open in another tab.

6. Enter user name and password for authentication. Once authenticated, browser tab gets closed and swagger-ui becomes fully authenticated.