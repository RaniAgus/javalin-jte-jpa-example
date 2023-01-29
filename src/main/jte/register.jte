@param io.github.raniagus.example.view.RegisterViewModel vm

@template.utils.form(
title = @`Register`,
styles = @``,
action = @`Create user`,
postUrl = "/register",
children = @`
    <label for="first-name">First name</label>
    <input type="text" id="first-name" name="first-name"
           placeholder="First name" value="${vm.getFirstName()} "required autofocus>

    <label for="last-name">Last name</label>
    <input type="text" id="last-name" name="last-name"
           placeholder="Last name" value="${vm.getLastName()}" required>

    <label for="email">Email</label>
    <input type="text" id="email" name="email"
           placeholder="Email" value="${vm.getEmail()}" required>

    <label for="password">Password</label>
    <input type="password" id="password" name="password"
           placeholder="Password" required>

    <label for="confirm-password">Confirm password</label>
    <input type="password" id="confirm-password" name="confirm-password"
           placeholder="Confirm password" required>

    @for (String error : vm.getErrors())
        <div role="alert">${error}</div>
    @endfor
`,
scripts = @``)
