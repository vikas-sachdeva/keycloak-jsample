##Prerequisites

1. Create realm in keycloak server
2. Create first client application with Access Type *public* for getting *access_token*
3. Create second client application with Access Type *bearer-only*
4. Create 2 roles *user* and *admin*
5. *admin* role should be of composite type. It should have 2 associated roles - *manage-users* and *view-users* . These 2 roles are available for *realm-management* client 
5. Create first user with role *user*
6. Create second user with role *admin*


##Execution Steps

1. Admin will have access on all these user management APIs

2. User will have access on any of these user management API 

3. All APIs can be executed by getting *access_token* from keycloak server and then passing *access_token* in *Authorization* header