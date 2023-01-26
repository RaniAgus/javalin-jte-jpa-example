@import gg.jte.Content

@param Content title
@param Content styles
@param Content action
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
            ${children}
            <button class="btn btn-lg btn-primary btn-block form-action" type="submit">${action}</button>

        </form>
    </main>
    </body>
`,
scripts = scripts)
