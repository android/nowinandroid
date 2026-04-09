#!/usr/bin/env bash
#
# This script automates the process of updating benchmark branches against a
# new base (usually 'main'). It iterates through a predefined list of
# "old" benchmark branches and performs a git rebase for each.
#
# By default, the script runs in DRY_RUN mode. Use -f or --force to
# execute the actual git commands.

set -euo pipefail

readonly OLD_BASES=(
	# Normal branches (No Regression)
	"bench-startup-normal"
	"bench-scrollforyou-normal"
	"bench-scrollforyou-mem-normal"

	# Regression branches
	"bench-startup-reg"
	"bench-scrollforyou-reg"
	"bench-scrollforyou-mem-reg"
)

NEW_BASE="main"

DRY_RUN=true

print_usage() {
	cat <<EOF
Usage: $(basename "$0") [OPTIONS]

Automates rebasing benchmark branches onto a new base branch.

Options:
  -h, --help           Show this help message and exit
  -f, --force          Disable dry-run mode and execute git commands
  --new-base <base>    Specify the base branch to rebase onto (default: $NEW_BASE)
EOF
}

while [[ $# -gt 0 ]]; do
	case "$1" in
	-h | --help)
		print_usage
		exit 0
		;;
	-f | --force)
		DRY_RUN=false
		shift
		;;
	--new-base)
		NEW_BASE="$2"
		shift 2
		;;
	*)
		echo "$(basename "$0"): invalid option -- '$1'"
		echo "Try '$(basename "$0") --help' for more information"
		exit 1
		;;
	esac
done

if "${DRY_RUN}"; then
	echo "info: running in dry run mode, the following commands will not be executed"
	echo "info: use -f or --force to execute the actual git commands"
fi

for BASE in "${OLD_BASES[@]}"; do
	if ${DRY_RUN}; then
		echo "dry: git checkout ${BASE}"
		echo "dry: git merge ${NEW_BASE}"
	else
		echo "git checkout ${BASE}"
		git checkout "${BASE}"

		echo "git merge ${NEW_BASE}"
		git merge "${NEW_BASE}"
	fi
done
