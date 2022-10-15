@param io.github.raniagus.project.view.LoginViewModel vm

@template.user-form(
title = @`Login`,
styles = @``,
error = vm.getError(),
postUrl = "/login",
children = @`
    <input type="hidden" name="redirect" value="${vm.getRedirect()}">
`,
scripts = @``)
