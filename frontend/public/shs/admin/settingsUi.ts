let subjects: string[] = [];

function settingsUI() {
    edom.fromTemplate(
        [
            {
                tag: 'h1',
                text: 'Fächer:',
            },
            {
                tag: 'button',
                classes: ['primaryButton'],
                text: 'bearbeiten',
                handler: [
                    {
                        type: 'click',
                        id: 'clickEdit',
                        arguments: '',
                        body: 'openEditSubjectsPopup()',
                    },
                ],
            },
            {
                tag: 'div',
                id: 'sectionSubjects',
            },
            {
                tag: 'h1',
                text: 'Klassenstufen:',
            },
        ],
        edom.findById('content')
    );
    setTimeout(() => loadSubjects(), 10);
}

function openEditSubjectsPopup() {
    setTimeout(() => renderSubjectsTable(), 10);
    popup('', {
        tag: 'div',
        children: [
            {
                tag: 'button',
                text: 'Fach hinzufügen',
                classes: ['primaryButton'],
                handler: [
                    {
                        type: 'click',
                        id: 'clickAdd',
                        arguments: '',
                        body: 'openPopupAddNew()',
                    },
                ],
            },
            {
                tag: 'button',
                text: 'Fächer speichern',
                classes: ['primaryButton'],
                handler: [
                    {
                        type: 'click',
                        id: 'clickSave',
                        arguments: 'self',
                        body: 'saveSubjects(self)',
                    },
                ],
            },
            {
                tag: 'table',
                id: 'tblEditSubjects',
            },
        ],
    });
}

function renderSubjectsTable() {
    const table: edomElement = edom.findById('tblEditSubjects')!;
    while (table.children.length > 0) {
        table.clear();
    }

    edom.fromTemplate(
        subjects.map((subject: string, index: number) => {
            return {
                tag: 'tr',
                children: [
                    {
                        tag: 'td',
                        children: [
                            {
                                tag: 'button',
                                text: '-',
                                handler: [
                                    {
                                        type: 'click',
                                        id: 'clickRemove',
                                        arguments: '',
                                        body: `subjects.splice(${index}, 1); renderSubjectsTable()`,
                                    },
                                ],
                            },
                        ],
                    },
                    {
                        tag: 'td',
                        children: [
                            {
                                tag: 'label',
                                text: subject,
                            },
                        ],
                    },
                ],
            };
        }),
        table
    );
}

function openPopupAddNew() {
    let name: string = '';
    popup('neues Fach', {
        tag: 'div',
        children: [
            {
                tag: 'input',
                handler: [
                    {
                        id: 'onChange',
                        type: 'input',
                        arguments: 'self',
                        body: 'name = self.value',
                    },
                ],
            },
            {
                tag: 'button',
                text: 'hinzufügen',
                classes: ['primaryButton'],
                handler: [
                    {
                        id: 'onSave',
                        type: 'click',
                        arguments: 'self',
                        body: 'subjects.push(name); renderSubjectsTable(); closePopup(self)',
                    },
                ],
            },
        ],
    });
}

function loadSubjects() {
    fetch(`${backend}/api/shs/subjects`, {})
        .then((response: Response) => {
            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status} ${
                        response.statusText
                    } - ${response.text()}`
                );
            }
            return response.json();
        })
        .then((subs: string[]) => {
            subjects = subs;
            renderSubjects();
        })
        .catch((e: any) => {
            console.error(e);
        });
}

function renderSubjects() {
    const container: edomElement = edom.findById('sectionSubjects')!;

    while (container.children.length > 0) {
        container.clear();
    }

    edom.fromTemplate(
        [
            {
                tag: 'ul',
                children: subjects.map((sub: string) => {
                    return {
                        tag: 'li',
                        text: "- " + sub,
                    };
                }),
            },
        ],
        container
    );
}

function saveSubjects(sender: edomElement) {
    fetch(`${backend}/api/shs/admin/subjects`, {
        method: 'PUT',
        headers: {
            Authorization: `Bearer ${localStorage.getItem('csd_token')}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(subjects),
    })
        .then((response: Response) => {
            closePopup(sender);
            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status} ${
                        response.statusText
                    } - ${response.text()}`
                );
            }
            loadSubjects();
        })
        .catch((e: any) => {
            console.error(e);
        });
}
