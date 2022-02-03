The "It's not stupid if it works" approach to creating a cross-platform library that provides an embedded `ChromeDriver`.
It will simply set everything up and then return a `WebDriver` instance.
This is meant for JVM applications that require web-scraping.

This library has 1 public function:
`getEmbeddedChromeDriver`

It has 2 optional parameters:
- `installDirectory`:
  The installation directory specifies where the Chromium and ChromeDriver should be installed.
  If none is specified it will automatically install it in a temporary location that the operating system will **wipe upon reboot.**
  It **won't reinstall** everything if it's already at the specified location.

- `configureOptions`:
  An optional lambda that sets the options for the ChromeDriver.
  It will be supplied with a `ChromeOptions` object upon it can set anything that is required.

The system property detailing the location of the `WebDriver` is set to the installation directory.
**It is reverted to the original value when the JVM shuts down**.

### Disclaimers
- Currently, **only Linux** is supported.
- The actual resources containing the browser binary and the WebDriver and the final `.jar` itself **aren't included** in the repository yet.
  Because they exceed the 100MB size limit of the GitHub repository, they will probably be hosted somewhere else.