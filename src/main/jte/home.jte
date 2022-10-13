@param io.github.raniagus.project.view.HomeViewModel vm

@template.base(
title = @`Home`,
styles = @``,
scripts = @``,
body = @`
    <body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h1>Hello, ${vm.getUsername()}!</h1>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <button class="btn btn-primary" onclick="window.location.href='/logout'">
                        Logout
                    </button>
                </div>
            </div>
        </div>
    </body>
`)
