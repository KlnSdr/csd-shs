function loadSubjectsFromBackend(): Promise<void> {
    return new Promise((resolve, reject) => {
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
                const subjectsStudent: HTMLDivElement = document.getElementById(
                    'optionsSubjectsStudent'
                ) as HTMLDivElement;
                const subjectsTeacher: HTMLDivElement = document.getElementById(
                    'optionsSubjectsTeacher'
                ) as HTMLDivElement;

                const container: HTMLDivElement[] = [
                    subjectsStudent,
                    subjectsTeacher,
                ];

                subs.forEach((sub: string) => {
                    container.forEach((parent: HTMLDivElement) => {
                        const option: HTMLDivElement =
                            document.createElement('div');
                        option.classList.add('option');

                        const input: HTMLInputElement =
                            document.createElement('input');
                        input.type = 'radio';
                        input.classList.add('radio');
                        input.value = sub;
                        input.name = sub;

                        const label: HTMLLabelElement =
                            document.createElement('label');
                        label.htmlFor = sub;
                        label.innerText = sub;

                        option.appendChild(input);
                        option.appendChild(label);

                        parent.appendChild(option);
                    });
                });
                resolve();
            })
            .catch((e: any) => {
                console.error(e);
                resolve();
            });
    });
}
