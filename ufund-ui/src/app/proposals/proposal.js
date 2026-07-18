
const NeedsValues = [
    "Name",
    "Cost",
    "Quantity",
    "Type",
    "Organization",
    "Submitted By",
    "Date",
    "Actions"
]

var typeProposal = "proposal"

const proposalValues = 
[
    "name",
    "cost",
    "quantity",
    "type",
    "organization",
    "username",
    "creationDate"
]

var listTitles = []
var listProposal = []
function Titles() {
    for (let values in NeedsValues) {
        listTitles.push(`<th>${values}</th>`)
    }
    document.getElementById("Titles").innerHTML = listTitles.join("");

}

function Proposal() {
    for (let values in proposalValues) {
        listProposal.push(`<td *ngIf="selectedProposal?.id !== proposal.id"> {{${typeProposal}.${proposalValues}}<td>`)
    }
    document.getElementById("Proposal2").innerHTML = listProposal.join("");
    
}
