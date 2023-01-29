@param io.github.raniagus.example.view.HomeViewModel vm

@template.base(
title = @`Home`,
styles = @``,
body = @`
    <body>
        <main>
            <section>
                <h1>Hello, ${vm.getFirstName()}!</h1>
                @if(vm.isAdmin())
                    <a href="/register">Register User</a>
                @endif
                <form method="post" action="/logout">
                    <button>Logout</button>
                </form>
            </section>
        </main>
    </body>
`,
scripts = @``)
