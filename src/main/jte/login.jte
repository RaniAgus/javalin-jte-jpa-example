@param io.github.raniagus.project.view.LoginViewModel vm

@template.base(
title = @`Login`,
styles = @`<link rel="stylesheet" href="/css/login.css" />`,
body = @`
    <body class="body-signin">
    <div class="container">
        <form class="form-signin" action="/login" method="post">
            <h2 class="form-signin-heading">Sign in</h2>
            <label for="username" class="sr-only">Username</label>
            <input class="form-control" type="text" id="username" name="username"
                   placeholder="Email address" required autofocus>
            <label for="password" class="sr-only">Password</label>
            <input class="form-control" type="password" id="password" name="password"
                   placeholder="Password" required>
            <input type="hidden" name="redirect" value="${vm.getRedirect()}">
            <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
            @if(vm.getError() != null)
                <div class="alert alert-danger" role="alert">
                    ${vm.getError()}
                </div>
            @endif
        </form>
    </div>
    </body>
`,
scripts = @``)
