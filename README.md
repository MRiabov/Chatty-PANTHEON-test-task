# REST API Description:
## /register
Registers a user to the server. 

Pass a String `username`, which is your desired username. A header `"Host"`, should be your IP.
## /quit
Quit from the server. Users can no longer write you, and you aren't shown in the list.

Pass your `username` to do it.

## /getAllUsers
Get the user list. It will help in the next command.

## /talkTo
Set to whom you wish to send your further messages. If you haven't registered, or the user you are talking to wasn't found, will send an error.

Pass a String `recipientName` to change your user. You can't talk to yourself. Header `"Host"` should be there as well.
## /sendMessage
Send a message to those, who you have chosen to send with /talkTo. Pass a String `text`, and the header. If you haven't registered, will send an error.

That's it. I actually loved doing this, because I knew I needed more practice in REST. Thank you! <sub>(and I hope we meet on the job.)</sub>
