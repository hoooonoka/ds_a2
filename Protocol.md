# Server-Client Message Exchange Protocol

## Note

**Use message generating functions in JsonParser.java to generate message**

## Login

1. **Client** sends a message to **Server**: message generating function *generateJsonLogin(String usernames)*

2. **Server** sends a reply message to **Client**:

- login success message: message generating function *generateJsonLoginSuccess(String[] usernames)*

- login fail message: message generating function *generateJsonLoginFail(String reason)*

## Logout


1. **Client** sends a message to **Server**: message generating function *generateJsonLogout(String usernames)*

2. **Server** sends a reply message to other **Clients**: message generating function *generateJsonTerminateGame( int gameID, String user)*

## Create Game

1. **Client** sends a message to **Server** to invite other users to create a game: message generating function *generateJsonCreateGame(String[] usernames)*

2. **Server** sends a invitation message to all other **Clients**: message generating function *generateJsonInvitation(int gameID, String users)*

3. Other **Clients** send a invitation reply message to **Server**: message generating function *generateJsonInvitationReply(String user, int gameID, boolean reply)*

- if a **Client** refuse the invitation or has been in a game, **Server** will sends a message informing the host: message generating function *generateJsonRefuseInvitation(int gameID, String users, String host, String reason)*

4. **Server** sends a create game reply message to all accept invitation **Clients** and host: message generating function *generateJsonCreateGameReply(String user, int gameID, boolean reply, String[] players)*

## Operate(including vote)

1. **Client** sends a message to **Server** informing the new operation: message generating function *generateJsonOperation(Operation operation, int gameID)*

2. **Server** sends a message to all other **Clients** informing them about the new operation: message generating function *generateJsonUpdateGame( int gameID, String user, Game game)*

- if operation=pass, continue game without voting

3. **Server** sends a message to all other **Clients** requesting them for voting: message generating function *generateJsonVote( int gameID, String user)*

4. **Clients** send a reply message to **Server**: message generating function *generateJsonVoteReply( int gameID, boolean reply, String user)*

5. **Server** sends a message to all **Clients** informing them voting result: message generating function *generateJsonVoteReplyToClients( int gameID, String user, boolean result)*

## Terminate

1. **Server** sends a message to all **Clients** to terminate a game: message generating function *generateJsonTerminateGame( int gameID, String user)*
