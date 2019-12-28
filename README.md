# SQL Developer KeepAlive for database connections
## What is it
This extension just tries to help you keep the connections open and your sessions valid. Works as your old JDBC connection pools, where a test query is sent to let the DB know that a session is still valid, regardless of how often you use it.

Since old Oracle SQL Developer versions, this check avoids having an unresponsive program because it hangs waiting for an answer from the server.

If you are using new JDBC 4.0 drivers, then it is very likely that they are validating the connection status without this trick. Still, even in newer SQL Dev versions, it seems like this can be useful in certain environments where communication is poor and SQL Dev logic seems to fall short.  

### What has changed?

For v1.4:
- The project structure changed to make it more similar to the [Oracle examples](https://github.com/oracle/oracle-db-examples/blob/master/sqldeveloper/extension/setup.md). This was made to ensure the resulting extension works with SQL Developer v19.
- Handling of PreparedStatement and ResultSet changed to avoid memory leaks.
- Some inline Strings across the code were moved to constants.
- The logging code was moved to a helper class to allow its usage across the extension. 

### How to install?

Use the [ZIP file](https://github.com/alfabravoteam/SQLDeveloper-keepalive/releases/download/1.4.0/keepalive-1.4.0.zip) (java/keepalive/build/cfu folder if you build it yourself) and perform a local installation through the `Search for Updates` panel.

### How to changes things I would like to see improve?

Fork it, send the pull request, done. I'm just getting a grasp on this code so any help is most welcome! Or raise an issue, it's your call.

Mind the fact this is heavily based on the [example project](https://github.com/oracle/oracle-db-examples/blob/master/sqldeveloper/extension/setup.md) from Oracle, so all building is done using Ant and I worked on it as a Eclipse project. I even left the setup.md file as a guide but you can check that project yourself for reference. 
 
### Disclaimer

This extension is intended only to be used as a helper while you work in SQL Developer and run your queries on it. It is NOT intended to be used in any other scenario nor will I provide any support for it; this extension is provided "AS IS", WITHOUT WARRANTY OF ANY KIND and, as the previous versions and maintainers did, the project is available in this repository with an MIT license.
