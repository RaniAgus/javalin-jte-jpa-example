@param io.github.raniagus.example.view.LoginViewModel vm

@template.utils.form(
title = @`Login`,
styles = @``,
action = @`Login`,
postUrl = "/login",
children = @`
    <input type="hidden" name="redirect" value="${vm.getRedirect()}">

    <label for="email">Email</label>
    <input type="text" id="email" name="email" placeholder="Email" required autofocus>

    <label for="password">Password</label>
    <input type="password" id="password" name="password" placeholder="Password" required>

    @if(vm.getError() != null)
        <div role="alert">${vm.getError()}</div>
    @endif
`,
scripts = @``)
