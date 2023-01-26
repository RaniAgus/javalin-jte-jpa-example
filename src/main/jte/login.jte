@param io.github.raniagus.example.view.LoginViewModel vm

@template.utils.form(
title = @`Login`,
styles = @``,
action = @`Login`,
postUrl = "/login",
children = @`
    <input type="hidden" name="redirect" value="${vm.getRedirect()}">

    <label for="email" class="sr-only">Email</label>
    <input class="form-control" type="text" id="email" name="email"
           placeholder="Email" required autofocus>

    <label for="password" class="sr-only">Password</label>
    <input class="form-control" type="password" id="password" name="password"
           placeholder="Password" required>

    @if(vm.getError() != null)
        <div class="alert alert-danger" role="alert">${vm.getError()}</div>
    @endif
`,
scripts = @``)
