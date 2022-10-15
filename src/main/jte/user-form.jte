@import gg.jte.Content

@param Content title
@param Content styles
@param String error
@param String postUrl
@param Content children
@param Content scripts

@template.base(
title = title,
styles = @`<link rel="stylesheet" href="/css/userForm.css" /> ${styles}`,
body = @`
    <body class="body-signin">
    <main class="container">
        <form class="form-signin" action="${postUrl}" method="post">
            <h2 class="form-signin-heading">${title}</h2>
            <label for="username" class="sr-only">Username</label>
            <input class="form-control" type="text" id="username" name="username"
                   placeholder="Username" required autofocus>
            <label for="password" class="sr-only">Password</label>
            <input class="form-control" type="password" id="password" name="password"
                   placeholder="Password" required>
            ${children}
            <button class="btn btn-lg btn-primary btn-block form-action" type="submit">Register</button>
            @if(error != null)
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            @endif
        </form>
    </main>
    </body>
`,
scripts = scripts)