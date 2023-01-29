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
    <body>
    <main>
        <form action="${postUrl}" method="post">
            <h2>${title}</h2>
            ${children}
            <button type="submit">${action}</button>
        </form>
    </main>
    </body>
`,
scripts = scripts)
